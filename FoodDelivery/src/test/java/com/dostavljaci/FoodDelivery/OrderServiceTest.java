package com.dostavljaci.FoodDelivery;

import com.dostavljaci.FoodDelivery.entity.*;
import com.dostavljaci.FoodDelivery.repository.OrderItemRepository;
import com.dostavljaci.FoodDelivery.repository.OrderRepository;
import com.dostavljaci.FoodDelivery.service.GeocodeService;
import com.dostavljaci.FoodDelivery.service.MenuItemService;
import com.dostavljaci.FoodDelivery.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuItemService menuItemService;

    @Mock
    private GeocodeService geocodeService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testAddMenuItemToOrder() {
        UUID menuItemId = UUID.randomUUID();
        Order mockOrder = new Order();
        mockOrder.setId(UUID.randomUUID());
        mockOrder.setRestaurant(new Restaurant());
        mockOrder.getRestaurant().setAddress(new ArrayList<Address>());
        mockOrder.getRestaurant().getAddress().add(new Address());
        mockOrder.setUser(new User());
        mockOrder.getUser().setAddress(new Address());

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockOrder));
        when(menuItemService.getMenuItemById(menuItemId)).thenReturn(new MenuItem());
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order result = orderService.addMenuItemToOrder(mockOrder, menuItemId);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class)); // Ensure the order is saved after adding the item
    }

    @Test
    public void testConfirmOrder() {
        Date expectedDeliveryTime = new Date();


        when(geocodeService.getScheduledDeliveryTime(any(Restaurant.class), any(Address.class)))
                .thenReturn(expectedDeliveryTime.getTime());
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());


        User user = new User();
        user.setAddress(new Address());
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(new ArrayList<>());
        restaurant.getAddress().add(new Address());
        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setUser(user);
        order.setId(UUID.randomUUID());


        orderService.confirmOrder(order);


        verify(orderRepository).save(any(Order.class));
    }
}
