import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:flutter_shop/data/model/product.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class ProductItem extends StatelessWidget {
  const ProductItem(
      {Key? key, required this.onOpenProduct, required this.product})
      : super(key: key);

  final Function(Product product) onOpenProduct;
  final Product product;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onOpenProduct(product),
      child: Card(
        elevation: 2.0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(
            16.0,
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            children: <Widget>[
              Expanded(
                child: ClipRRect(
                  clipBehavior: Clip.antiAliasWithSaveLayer,
                  borderRadius: BorderRadius.vertical(
                    top: Radius.circular(16),
                  ),
                  child: Image.network(
                    product.image,
                    height: 100,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
              SizedBox(
                height: 8,
              ),
              Expanded(
                child: Column(
                  children: [Expanded(child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text(product.title, style: TextStyle(fontSize: 12),),
                  ))],
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
