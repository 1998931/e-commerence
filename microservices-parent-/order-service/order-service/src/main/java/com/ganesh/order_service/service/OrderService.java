package com.ganesh.order_service.service;


import com.ganesh.order_service.dto.InventoryRespone;
import com.ganesh.order_service.dto.OrderLineItemDtos;
import com.ganesh.order_service.dto.OrderRequest;
import com.ganesh.order_service.model.Order;
import com.ganesh.order_service.model.OrderLineItems;
import com.ganesh.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    public OrderService(RestTemplate restTemplate, OrderRepository orderRepository) {
        this.restTemplate = restTemplate;

        this.orderRepository = orderRepository;
    }

    public void placeOrder(OrderRequest orderRequest) {

        Order order = new Order();

       order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> list = orderRequest.getOrderLineItemDtosList().stream()
                .map(this::mapToDto).toList();
        order.setOrderLineItems(list);
        List<String> skoCode= order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();

        //System.out.println(skoCode+" "+list+" "+order);
        // Call inventory service place order if
        //product is available
        String baseUrl= "http://inventory-service/api/inventory";
        URI uri= UriComponentsBuilder.fromUriString(baseUrl).queryParam("skoCode",skoCode.toArray()).build().toUri();
        //System.out.println(uri);
        InventoryRespone[] inventoryResponesArray = restTemplate.getForObject(uri, InventoryRespone[].class);
        //System.out.println(inventoryResponesArray);
        assert inventoryResponesArray != null;
        boolean allProductAreInStock= Arrays.stream(inventoryResponesArray).allMatch(InventoryRespone::isInStock);

        //   Boolean forObject = restTemplate.getForObject("http://localhost:8082/api/inventory",uriBuilder->uriBuilder.qu, Boolean.class);
if (allProductAreInStock) {

    orderRepository.save(order);

}
else{
    throw new IllegalArgumentException("Product is not available");
}


    }

    private OrderLineItems mapToDto(OrderLineItemDtos orderLineItemDtos) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemDtos.getPrice());
        orderLineItems.setQuantity(orderLineItemDtos.getQuantity());
        orderLineItems.setSkuCode(orderLineItemDtos.getSkuCode());
        return orderLineItems;
    }
}
