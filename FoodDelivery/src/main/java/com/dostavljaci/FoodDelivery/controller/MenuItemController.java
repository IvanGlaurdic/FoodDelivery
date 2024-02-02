package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.*;
import com.dostavljaci.FoodDelivery.service.MenuItemService;
import com.dostavljaci.FoodDelivery.service.OrderService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Controller
@AllArgsConstructor
@RequestMapping("/menu-items")
public class MenuItemController {
    public final MenuItemService menuItemService;
    public final RestaurantService restaurantService;
    public final UserService userService;
    public final OrderService orderService;


    @GetMapping("/{restaurantName}")
    public String restaurantInfo(Model model,
                                 @PathVariable String restaurantName,
                                 HttpSession session){
        Restaurant restaurant=restaurantService.getRestaurantByName(restaurantName);
        System.out.print(restaurant);
        List<MenuItem> menuItem= menuItemService.getMenuByRestaurantId(restaurant.getId());
        List<Order> orders = orderService.getOrdersByRestaurant(restaurant);

        model.addAttribute("orders", orders);
        model.addAttribute("restaurant",restaurant);
        model.addAttribute("restaurantName",restaurantName);
        model.addAttribute("menuItem", menuItem);

        return "restaurant-info";

    }

    @GetMapping("/add-item/{restaurantName}")
    public String showMenuItemForm(Model model, @PathVariable String restaurantName) {

        model.addAttribute("address", new MenuItem());
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("error", null);
            return "add-menuitem";
    }

    @PostMapping("/add-item/{restaurantName}")
    public String handleMenuItemSubmission(@ModelAttribute MenuItem menuItem,
                                           Model model, HttpSession session,
                                           @PathVariable String restaurantName) {

        Object sessionUser = session.getAttribute("user");
        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);

        if (sessionUser instanceof User userInstance) {
            if (Objects.equals(userService.getUserByUsername(userInstance.getUsername()).getRole().toLowerCase(), "admin")
                    || Objects.equals(userService.getUserById(userInstance.getId()), restaurant.getOwner())) {

                menuItem.setRestaurant(restaurant);
                menuItemService.saveMenuItem(menuItem);

                return "redirect:/menu-items/" + restaurant.getName();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/{restaurantName}/edit-menuitem/{menuItemId}")
    public String showEditMenuItemForm(@PathVariable String restaurantName,
                                       @PathVariable UUID menuItemId, Model model, HttpSession session) {

        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);

        model.addAttribute("menuItem", menuItem);
        model.addAttribute("restaurant",restaurant);
        model.addAttribute("error",null);
        return "edit-menuitem";


    }

    @PostMapping("/{restaurantName}/edit-menuitem/{menuItemId}")
    public String handleEditMenuItemSubmission(@ModelAttribute MenuItem menuItem,
                                               Model model, HttpSession session,
                                               @PathVariable String restaurantName,
                                               @PathVariable UUID menuItemId) {

        Object sessionUser = session.getAttribute("user");
        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);
        MenuItem requestedMenuItem = menuItemService.getMenuItemById(menuItemId);

        System.out.print(sessionUser);

        if (sessionUser instanceof User userInstance) {
            if (Objects.equals(userService.getUserByUsername(userInstance.getUsername()).getRole().toLowerCase(), "admin")
                    || Objects.equals(userService.getUserById(userInstance.getId()), restaurant.getOwner())) {

             


                boolean isUpdated = updateIfChanged(requestedMenuItem::getName, requestedMenuItem::setName, menuItem.getName())
                        | updateIfChanged(requestedMenuItem::getDescription, requestedMenuItem::setDescription, menuItem.getDescription())
                        | updateIfChanged(requestedMenuItem::getImageURL, requestedMenuItem::setImageURL, menuItem.getImageURL())
                        | updateIfChanged(requestedMenuItem::getPrice, requestedMenuItem::setPrice, menuItem.getPrice())
                        | updateIfChanged(requestedMenuItem::getCategory, requestedMenuItem::setCategory, menuItem.getCategory());




                if (isUpdated){

                    menuItemService.saveMenuItem(requestedMenuItem);

                    return "redirect:/menu-items/" + restaurantName;
                }

            }
            return "redirect:/profile/" + userInstance.getUsername();
        }

        return "redirect:/";
    }


    @PostMapping("/{restaurantName}/delete/{menuItemId}")
    public String deleteMenuItem(
            @PathVariable UUID menuItemId,
            @PathVariable String restaurantName,
            RedirectAttributes redirectAttributes,
            HttpSession httpSession,
            Model model) {
        try {


            menuItemService.deleteMenuItemById(menuItemId);

            redirectAttributes.addFlashAttribute("successMessage", "Restaurant deleted successfully!");
            return "redirect:/menu-items/" + restaurantName;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting restaurant.");
            return "redirect:/menu-items/" + restaurantName;
        }
    }

    private <T> boolean updateIfChanged(Supplier<T> getter, Consumer<T> setter, T newValue) {
        if (!Objects.equals(getter.get(), newValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

}
