import 'dart:convert';
import 'package:flutter_shop/data/model/product.dart';
import 'package:flutter_shop/util/constants.dart';
import 'package:http/http.dart' as http;

class FakeStoreApiService {
  static Future<List<Product>> getProducts() async {
    try {
      var client = http.Client();
      var response = await client.get(Uri.parse("$kBaseUrl/products"));
      if (response.statusCode == 200) {
        return (jsonDecode(response.body) as List)
            .map((product) => Product.fromJson(product))
            .toList();
      } else {
        throw Exception("Couldn't fetch data!");
      }
    } catch (e) {
      rethrow;
    } finally {}
  }
}
