package com.veggkart.android.util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.veggkart.android.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-08-02
 * Project: veggkart
 */
public class APIHelper {
  private static final String baseUrl = "http://nullsquad.in";
  private static final String endpointProducts = "/api/crud_api.php";
  private static final String endpointPlaceOrder = "/api/upload_1.php";
  private static final String endpointSignIn = "/api/login_api.php";

  public static String getEndpointProducts() {
    return APIHelper.baseUrl + APIHelper.endpointProducts;
  }

  public static String getEndpointPlaceOrder() {
    return APIHelper.baseUrl + APIHelper.endpointPlaceOrder;
  }

  public static String getProductImageUrl(String imageUrlStub) {
    return APIHelper.baseUrl + imageUrlStub;
  }

  public static String getEndpointSignIn() {
    return APIHelper.baseUrl + APIHelper.endpointSignIn;
  }

  public static void placeOrder(ArrayList<Product> products, Context context, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
    try {
      String userId = UserHelper.getUserId(context);
      JSONObject tempParameter = new JSONObject();
      tempParameter.put("userDetails", new JSONObject("{\"userid\":\"" + userId + "\"}"));
      JSONArray productsJson = new JSONArray();
      for (int i = 0; i < products.size(); i++) {
        JSONObject tempProduct = new JSONObject();
        tempProduct.put("quantity", products.get(i).getQuantity());
        tempProduct.put("proid", Integer.parseInt(products.get(i).getId()));
        productsJson.put(tempProduct);
      }
      tempParameter.put("orderList", productsJson);
      JSONObject parameters = new JSONObject();
      parameters.put("UArray", tempParameter);

      JsonObjectRequest orderProductsRequest = new JsonObjectRequest(Request.Method.POST, APIHelper.getEndpointPlaceOrder(), parameters, responseListener, errorListener);
      VolleySingleton.getInstance(context).addToRequestQueue(orderProductsRequest);
    } catch (JSONException e) {
      e.printStackTrace();
      Toast.makeText(context, "Some error occurred\nPlease try again after some time", Toast.LENGTH_SHORT).show();
    }
  }

  public static void userSignIn(String username, String password, Context context, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
    try {
      String passwordHash = Helper.stringToMD5Hex(password);
      JSONObject tempParameter = new JSONObject();
      tempParameter.put("username", username);
      tempParameter.put("passHash", passwordHash);
      JSONObject parameter = new JSONObject();
      parameter.put("getIn", tempParameter);

      JsonObjectRequest signInRequest = new JsonObjectRequest(Request.Method.POST, APIHelper.getEndpointSignIn(), parameter, responseListener, errorListener);
      VolleySingleton.getInstance(context).addToRequestQueue(signInRequest);
    } catch (JSONException e) {
      e.printStackTrace();
      Toast.makeText(context, "Some error occurred\nPlease try again after some time", Toast.LENGTH_SHORT).show();
    }
  }
}
