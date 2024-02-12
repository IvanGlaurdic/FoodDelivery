package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Order;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Controller
@AllArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    public final RestaurantService restaurantService;
    public final AddressService addressService;
    public final GeocodeService geocodeService;
    public final UserService userService;
    public final OrderService orderService;

    @GetMapping()
    public String showRestaurantForm(Model model, HttpSession session) {
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        User user =(User) session.getAttribute("user");
        model.addAttribute("user", user);

        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("address", new Address());
        model.addAttribute("error", null);
        return "add-restaurant";
    }

    @PostMapping()
    public String handleRestaurantSubmission(
            @RequestParam String restaurantName,
            @RequestParam String contactNumber,
            @RequestParam String street,
            @RequestParam String city,
            @RequestParam String province,
            @RequestParam String country,
            @RequestParam String postalCode,
            @RequestParam("picture") MultipartFile file,
            Model model,
            HttpSession session) {

        Object sessionUser = session.getAttribute("user");
        User user;

        if (sessionUser instanceof User userInstance){
            user = userService.getUserByUsername(userInstance.getUsername());
        }
        else {
            throw new RuntimeException("Cant locate user:" + sessionUser);
        }

        //save the address and restaurant
        Address address = addressService.getAddressByAllParams(city,street,province,country,postalCode);
        if (address == null){
            address = addressService.saveAddress(city,street,province,country,postalCode);
        }

        Restaurant savedRestaurant =
                restaurantService.saveRestaurant(restaurantName,contactNumber,user,(float)0,address, saveFile(file));


        model.addAttribute(addressService.getAddressById(address.getId()));
        model.addAttribute(restaurantService.getRestaurantById(savedRestaurant.getId()));

        return "redirect:/profile/" + user.getUsername();
    }



    @PostMapping("/delete/{id}")
    public String deleteRestaurant(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes,
            HttpSession httpSession,
            Model model) {

        User user;

        Object sessionUser = httpSession.getAttribute("user");
        if (sessionUser instanceof User userInstance) {

            user = userService.getUserByUsername(userInstance.getUsername());
        } else {

            model.addAttribute("error", "User authentication failed.");
            return "redirect:/login";
        }

        try {

            restaurantService.deleteRestaurantById(id);

            redirectAttributes.addFlashAttribute("successMessage", "Restaurant deleted successfully!");
            return "redirect:/profile/" + user.getUsername();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting restaurant.");
            return "redirect:/profile/" + user.getUsername();
        }
    }


    @GetMapping("/edit-restaurant/{restaurantname}")
    public String editRestaurant(@PathVariable("restaurantname") String restaurantname,
                                 Model model, HttpSession session){

        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        User user =(User) session.getAttribute("user");
        model.addAttribute("user", user);

        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantname);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("error", null);
        return "edit-restaurant";

    }
    @PostMapping("/edit-restaurant/{restaurantname}")
    public String handleEditedProfile(@PathVariable("restaurantname") String requestedName,
                                      @RequestParam String Name,
                                      @RequestParam String ContactNumber,
                                      Model model,
                                      HttpSession session){
        Restaurant currentRestaurant = restaurantService.getRestaurantByName(requestedName);

        if (currentRestaurant == null) {
            return "redirect:/";
        }

        if (restaurantService.isUsernameTaken(Name, currentRestaurant.getId())) {
            model.addAttribute("restaurant", currentRestaurant);
            model.addAttribute("error", null);
            return "edit-restaurant";

        }


        boolean isUpdated = updateIfChanged(currentRestaurant::getName, currentRestaurant::setName, Name)
                | updateIfChanged(currentRestaurant::getContactNumber, currentRestaurant::setContactNumber, ContactNumber);

        if (isUpdated){
            restaurantService.saveRestaurant(currentRestaurant);
        }
        if (session.getAttribute("user") instanceof User sessionUser){
            return "redirect:/profile/" + sessionUser.getUsername();
        }

        return "redirect:/";


    }



    @PostMapping("/complete-order/{orderId}/{restaurantName}")
    public String updateOrderStatus(@PathVariable UUID orderId,@PathVariable String restaurantName){
        Order order = orderService.getOrderById(orderId);
        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);
        order.setStatus("completed");
        order.setScheduledDeliveryTime(
                orderService.calculateScheduledDeliveryTime(
                        order.getRestaurant(),
                        order.getUser().getAddress()
                )
        );
        orderService.saveOrder(order);

        return "redirect:/menu-items/"+restaurant.getName();
    }

    private boolean updateIfChanged(Supplier<String> getter, Consumer<String> setter, String newValue) {
        if (!Objects.equals(getter.get(), newValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }
    private String saveFile(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = Objects.requireNonNull(originalFileName)
                    .substring(originalFileName.lastIndexOf('.'));
            String uniqueFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                    + "_" + UUID.randomUUID() + fileExtension;

            // This is the path where the file will be saved
            Path savePath = Paths.get("src/main/resources/static/images/restaurants/" + uniqueFileName);

            // Ensure the directory exists
            Files.createDirectories(savePath.getParent());
            Files.copy(file.getInputStream(), savePath);

            return "images/restaurants/" + uniqueFileName;
        } catch (IOException e) {
            // Handle exception
            throw new RuntimeException("Failed to store file.", e);
        }
    }


}
