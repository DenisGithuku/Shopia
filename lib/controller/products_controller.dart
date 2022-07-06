import 'package:flutter_shop/data/model/product.dart';
import 'package:flutter_shop/data/service/fake_store_api_service.dart';
import 'package:get/get.dart';
import 'package:get/get_state_manager/src/simple/get_controllers.dart';

class ProductsController extends GetxController {
  RxList<Product> products = <Product>[].obs;

  RxBool isLoading = false.obs;

  @override
  void onInit() {
    getProducts();
    super.onInit();
  }

  getProducts() async {
    try {
      isLoading(true);
      products.value = await FakeStoreApiService.getProducts();
      // print("Products count ${products.value.length}");
    } finally {
      isLoading(false);
    }
  }
}
