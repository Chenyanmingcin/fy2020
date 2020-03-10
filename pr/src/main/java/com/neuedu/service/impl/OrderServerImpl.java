package com.neuedu.service.impl;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.OrderItemMapper;
import com.neuedu.dao.OrderMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.BigDecimalUtil;
import com.neuedu.utils.DateUtils;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import com.neuedu.vo.ProductDetailVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServerImpl implements IOrderService {

    @Resource
    OrderMapper orderMapper;
    @Resource
    ICartService cartService;
    @Resource
    IProductService productService;
    @Resource
    OrderItemMapper orderItemMapper;

    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {

        if(shippingId==null){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_NOT_EMPTY.getStatus(),StatusEnum.ADDRESS_NOT_EMPTY.getDesc());
        }

        ServerResponse serverResponse=cartService.findCartByIdAndChecked(userId);
        if(!serverResponse.isSuccess()){
           return serverResponse;
        }

        List<Cart> cartList= (List<Cart>) serverResponse.getData();
        if(cartList==null){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_CART_EMPTY.getStatus(),StatusEnum.USER_CART_EMPTY.getDesc());
        }
        ServerResponse<List<OrderItem>> serverResponse1=assembleOrderItemList(cartList,userId);
        if(!serverResponse1.isSuccess()){
            return serverResponse1;
        }

        //生成订单
        List<OrderItem> orderItemList=serverResponse1.getData();
        ServerResponse serverResponse2=generateOrder(userId,orderItemList,shippingId);

        if(!serverResponse2.isSuccess()){
            return serverResponse2;
        }

        //批量插入订单
        Order order= (Order) serverResponse2.getData();
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());

        }

        int count=orderItemMapper.insertBatch(orderItemList);
        if(count<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_ITEM_CREATE_FAIL.getStatus(),StatusEnum.ORDER_ITEM_CREATE_FAIL.getDesc());
        }
        reduceStock(orderItemList);

        ServerResponse serverResponse3=cleanCart(cartList);
        if(!serverResponse3.isSuccess()){
            return  serverResponse3;
        }

        OrderVO orderVO=assembleOrderVO(order,orderItemList,shippingId);


        return ServerResponse.serverResponseBySuccess(null,orderVO);
    }

    @Override
    public ServerResponse cancelOrder(Long orderNO) {

        if (orderNO==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }

        Order order=orderMapper.findOrderByOrderNO(orderNO);
        if(order==null){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_NOT_EXISTS.getStatus(),StatusEnum.ORDER_NOT_EXISTS.getDesc());
        }

        if(order.getStatus()!=Consts.OrderStatusEnum.UNPAY.getStatus()){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_CANT_CANCEL.getStatus(),StatusEnum.ORDER_CANT_CANCEL.getDesc());
        }
        order.setStatus(Consts.OrderStatusEnum.CANCELED.getStatus());
        int count=orderMapper.updateByPrimaryKey(order);
        if(count<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_CANCEL_FAIL.getStatus(),StatusEnum.ORDER_CANCEL_FAIL.getDesc());
        }
        List<OrderItem> orderItemList=orderItemMapper.findOrderItemByOrderNO(orderNO);

        for(OrderItem orderItem:orderItemList){
            Integer quantity=orderItem.getQuantity();
            Integer productId=orderItem.getProductId();
            ServerResponse serverResponse=productService.updateStock(productId,quantity,1);

            if(!serverResponse.isSuccess()){
                return ServerResponse.serverResponseByFail(StatusEnum.UPDATE_STOCK_FAIL.getStatus(),StatusEnum.UPDATE_STOCK_FAIL.getDesc());
            }

        }


        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse findOrderByOrderNO(Long oderNO) {

        if(oderNO==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }


        Order order=orderMapper.findOrderByOrderNO(oderNO);
        if(order==null){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_NOT_EXISTS.getStatus(),StatusEnum.ORDER_NOT_EXISTS.getDesc());
        }
        List<OrderItem> orderItemList=orderItemMapper.findOrderItemByOrderNO(oderNO);

        OrderVO orderVO=assembleOrderVO(order,orderItemList,order.getShippingId());

        return ServerResponse.serverResponseBySuccess(null,orderVO);
    }

    @Override
    public ServerResponse updateOrder(Long orderNo, String payTime, Integer orderStatus) {
        if(orderNo==null||payTime==null||orderStatus==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }


        int count=orderMapper.updateOrder(orderNo,DateUtils.str2Date(payTime),orderStatus);

        if(count<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_STATUS_FAIL.getStatus(),StatusEnum.ORDER_STATUS_FAIL.getDesc());
        }

        return ServerResponse.serverResponseBySuccess();
    }

    public OrderVO assembleOrderVO(Order order,List<OrderItem> orderItemList,Integer shippingId){
        OrderVO orderVO=new OrderVO();
        orderVO.setUserId(order.getUserId());
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentTime(DateUtils.date2Str(order.getPaymentTime()));
        orderVO.setSendTime(DateUtils.date2Str(order.getSendTime()));
        orderVO.setEndTime(DateUtils.date2Str(order.getEndTime()));
        orderVO.setCreateTime(DateUtils.date2Str(order.getCreateTime()));
        orderVO.setCloseTime(DateUtils.date2Str(order.getCloseTime()));

        orderVO.setShippingId(shippingId);

        List<OrderItemVO> orderItemVOList=new ArrayList<>();

        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO=convertOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }


        orderVO.setOrderItemVOList(orderItemVOList);


        return orderVO;
    }

    private OrderItemVO convertOrderItemVO(OrderItem orderItem){
        if(orderItem==null){
            return null;
        }
        OrderItemVO orderItemVO=new OrderItemVO();

        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItem.setProductId(orderItem.getProductId());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        orderItemVO.setCreateTime(DateUtils.date2Str(orderItem.getCreateTime()));


        return orderItemVO;
    }

    private  ServerResponse cleanCart(List<Cart> cartList){
        return  cartService.deleteBatchByIds(cartList);
    }

    private ServerResponse reduceStock(List<OrderItem> orderItemList){
        for(OrderItem orderItem:orderItemList){
            Integer productId=orderItem.getProductId();
            Integer quantity=orderItem.getQuantity();
            ServerResponse serverResponse=productService.updateStock(productId,quantity,0);
            if(!serverResponse.isSuccess()){
                return serverResponse;
            }
        }
        return ServerResponse.serverResponseBySuccess();
    }

    private ServerResponse generateOrder(Integer userId, List<OrderItem> orderItems, Integer shippingId){
        Order order=new Order();

        order.setUserId(userId);
        order.setShippingId(shippingId);

        order.setPayment(getOrderTotalPrice(orderItems));
        order.setPaymentType(1);
        order.setStatus(Consts.OrderStatusEnum.UNPAY.getStatus());
        order.setOrderNo(generateOrderNO());
        int count=orderMapper.insert(order);
        if(count<=0){
            ServerResponse.serverResponseByFail(StatusEnum.ORDER_CREATE_FAIL.getStatus(),StatusEnum.ORDER_CREATE_FAIL.getDesc());
        }
        return ServerResponse.serverResponseBySuccess(null,order);
    }

    private Long generateOrderNO(){
        return System.currentTimeMillis();
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
        BigDecimal orderTotalPrice =new BigDecimal("0");

        for(OrderItem orderItem:orderItems){
            orderTotalPrice=BigDecimalUtil.add(String.valueOf(orderTotalPrice),String.valueOf(orderItem.getCurrentUnitPrice()));
        }
        return orderTotalPrice;
    }

    private  ServerResponse assembleOrderItemList(List<Cart> cartList,Integer userId ){
        List<OrderItem> orderItemList = new ArrayList<>();

        for(Cart cart:cartList){
            OrderItem orderItem=new OrderItem();
            orderItem.setProductId(cart.getProductId());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setUserId(cart.getUserId());

            ServerResponse serverResponse=productService.detail(cart.getProductId());
            if(!serverResponse.isSuccess()){
                return serverResponse;
            }
            //在售
            ProductDetailVO productDetailVO= (ProductDetailVO) serverResponse.getData();
            if(productDetailVO.getStatus()!= Consts.ProductStatusEnum.PRODUCT_ONLINE.getStatus()){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getStatus(),StatusEnum.PRODUCT_NOT_EXISTS.getDesc());
            }
            //库存
            if(productDetailVO.getStock()<cart.getQuantity()){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_STOCK_NOT_FULL.getStatus(),StatusEnum.PRODUCT_STOCK_NOT_FULL.getDesc());
            }

            orderItem.setProductId(cart.getProductId());
            orderItem.setCurrentUnitPrice(productDetailVO.getPrice());
            orderItem.setProductImage(productDetailVO.getMainImage());
            orderItem.setProductName(productDetailVO.getName());
            orderItem.setTotalPrice(BigDecimalUtil.multi(String.valueOf(productDetailVO.getPrice().doubleValue()),String.valueOf(cart.getQuantity())));

            orderItemList.add(orderItem);


        }

        return ServerResponse.serverResponseBySuccess(null,orderItemList);
    }
}
