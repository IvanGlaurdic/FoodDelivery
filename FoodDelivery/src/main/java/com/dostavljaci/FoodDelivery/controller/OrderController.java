package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.DTO.OrderItemDTO;
import com.dostavljaci.FoodDelivery.entity.MenuItem;
import com.dostavljaci.FoodDelivery.entity.Order;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.MenuItemService;
import com.dostavljaci.FoodDelivery.service.OrderService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {
    public final OrderService orderService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;



    @GetMapping("/{Name}")
    public ModelAndView orderPage(@PathVariable String Name, Model model, HttpSession session) {
        session.setAttribute("currentOrder", null);

        User user = getUserFromSession(session);
        Restaurant restaurant = getRestaurantFromSession(session, Name);
        List<MenuItem> menu= menuItemService.getMenuByRestaurantId(restaurant.getId());


        model.addAttribute("user",user);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menu",menu);


        ModelAndView modelAndView=new ModelAndView("order-page");
        modelAndView.addObject("user",user);
        modelAndView.addObject("menu",menu);
        modelAndView.addObject("restaurant",restaurant);
        modelAndView.addObject("isLoggedIn",true);


        return modelAndView;
    }


    @PostMapping("/add-to-basket")
    @Transactional
    public ResponseEntity<List<OrderItemDTO>> addToBasket(@RequestBody Map<String, Object> payload, HttpSession session) {
        UUID menuItemId = UUID.fromString((String) payload.get("menuItemId"));
        String restaurantName = (String) payload.get("restaurantName");
        Order order = (Order) session.getAttribute("currentOrder");

        if (order == null) {
            User user = getUserFromSession(session);
            Restaurant restaurant = getRestaurantFromSession(session, restaurantName);
            order = orderService.setNewOrder(
                        Date.valueOf(LocalDate.now()),
                        user,
                        restaurant);

            session.setAttribute("currentOrder", order);
        }


        order = orderService.addMenuItemToOrder(order, menuItemId);

        session.setAttribute("currentOrder", order);
        System.out.print(order);

        // Create DTOs for the order items
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderItemDTOs);
    }

    @PostMapping("/remove-from-basket")
    @Transactional
    public ResponseEntity<List<OrderItemDTO>> removeFromBasket(@RequestBody Map<String, Object> payload, HttpSession session) {
        UUID menuItemId = UUID.fromString((String) payload.get("menuItemId"));

        session.setAttribute("currentOrder", orderService.removeMenuItemFromOrder((Order) session.getAttribute("currentOrder"), menuItemId));

        Order order = (Order) session.getAttribute("currentOrder");
        // Convert to DTOs and return
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderItemDTOs);
    }

    @PostMapping("/confirm")
    @Transactional
    public ResponseEntity<Void> confirmOrder(HttpSession session) {
        Order order = (Order) session.getAttribute("currentOrder");
        if (order != null) {
            orderService.confirmOrder(order);
            session.removeAttribute("currentOrder"); // Clear the session attribute after confirmation
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/order-rating/{orderId}")
    public ResponseEntity<Object> updateOrderRating(@PathVariable UUID orderId,
                                    @RequestParam Float rating,
                                    @RequestParam String comment,
                                    @RequestParam String restaurantName,
                                    Model model,
                                    HttpSession session){

        User user = getUserFromSession(session);
        Restaurant restaurant = getRestaurantFromSession(session, restaurantName);
        Order order = orderService.getOrderById(orderId);

        boolean ifUpdated = updateIfChanged(order::getRating, order::setRating, rating)
                | updateIfChanged(order::getComment, order::setComment, comment);

        if(ifUpdated){
            orderService.saveOrder(order);

            // Recalculate and update restaurant rating
            List<Order> restaurantOrders = orderService.getOrdersByRestaurant(restaurant);
            float totalRating = 0;
            int ratingCount = 0;
            for (Order restaurantOrder : restaurantOrders) {
                if (restaurantOrder.getRating() != 0) {
                    totalRating += restaurantOrder.getRating();
                    ratingCount++;
                }
            }
            if (ratingCount > 0) {
                float averageRating = totalRating / ratingCount;
                restaurant.setRating(averageRating);
                restaurantService.saveRestaurant(restaurant);
            }

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("redirect:/profile/"+user.getUsername()+"/orders"));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);

    }


    private <T> boolean updateIfChanged(Supplier<T> getter, Consumer<T> setter, T newValue) {
        if (!Objects.equals(getter.get(), newValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }



    public User getUserFromSession(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User is not logged in.");
        }
        return user;
    }




    public  Restaurant getRestaurantFromSession(HttpSession session, String name){
        Restaurant restaurant = restaurantService.getRestaurantByName(name);

        if(restaurant==null){
            restaurant = (Restaurant) session.getAttribute("restaurant");
            if (restaurant == null) {
                throw new RuntimeException("Restaurant with name " + name + " not found.");
            }
        }
        session.setAttribute("restaurant", restaurant);
        return restaurant;
    }





}
