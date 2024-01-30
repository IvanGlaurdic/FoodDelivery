package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.MenuItem;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {
    public final OrderService orderService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final UserService userService;
    private final OrderItemService orderItemService;


    @GetMapping("/{Name}")
    public ModelAndView orderPage(@PathVariable String Name, Model model, HttpSession session) {

        Object sessionUser = session.getAttribute("user");
        User user;
        if (sessionUser instanceof User userInstance){
            user = userService.getUserByUsername(userInstance.getUsername());
        }
        else {
            throw new RuntimeException("Cant locate user:" + sessionUser);
        }

        model.addAttribute("user",user);
        Restaurant restaurant=restaurantService.getRestaurantByName(Name);

        model.addAttribute("restaurant", restaurant);

        List<MenuItem> menu= menuItemService.getMenuByRestaurantId(restaurant.getId());
        System.out.print(menu);
        model.addAttribute("menu",menu);








        











        ModelAndView modelAndView=new ModelAndView("order-page");
        modelAndView.addObject("user",user);
        modelAndView.addObject("menu",menu);
        modelAndView.addObject("restaurant",restaurant);
        modelAndView.addObject("loggedIn",true);


        return modelAndView;
    }


}
