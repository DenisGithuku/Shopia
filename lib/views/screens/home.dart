import 'package:flutter/material.dart';
import 'package:flutter_shop/controller/products_controller.dart';
import 'package:flutter_shop/views/components/home_app_bar.dart';
import 'package:flutter_shop/views/components/product_item.dart';
import 'package:flutter_shop/views/screens/product_details.dart';
import 'package:get/get.dart';
import 'package:get/get_core/get_core.dart';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  final ProductsController productsController = Get.put(ProductsController());

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
          appBar: HomeAppBar(username: "Denis"),
          body: Obx(() => productsController.isLoading.value ?
              Center(child: CircularProgressIndicator())
           : GridView.builder(
            itemCount: productsController.products.length,
            itemBuilder: (context, index) {
              return ProductItem(
                onOpenProduct: (product) {
                  Get.to(() => ProductDetails(product: product));
                },
                product: productsController.products[index],);
            },
            gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
                maxCrossAxisExtent: 200,
                childAspectRatio: 3 / 2,
                crossAxisSpacing: 5,
                mainAxisSpacing: 5),
          ),
          ),
      ),
    );
  }
}
