import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

import '../../data/model/product.dart';

class ProductDetails extends StatelessWidget {
  const ProductDetails({Key? key, required this.product}) : super(key: key);

  final Product product;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(product.title),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Container(
            padding: EdgeInsets.all(8.0),
            color: Colors.white,
            height: 150,
            width: double.infinity,
            child: Hero(
              tag: "product-image",
              child: Image(
                colorBlendMode: BlendMode.darken,
                image: NetworkImage(product.image),
              ),
            ),
          ),
          Expanded(
            child: Container(
              decoration: BoxDecoration(
                  color: Colors.grey.withOpacity(0.2),
                  boxShadow: [
                    BoxShadow(
                        color: Colors.white70,
                        blurRadius: 2.0,
                        blurStyle: BlurStyle.outer)
                  ],
                  borderRadius: BorderRadius.only(
                    topRight: Radius.circular(24.0),
                    topLeft: Radius.circular(24.0),
                  ),
                  ),
              child: Padding(
                padding: const EdgeInsets.all(12.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      product.title,
                      style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold),
                      textAlign: TextAlign.center,
                    ),
                    SizedBox(
                      height: 8.0,
                    ),
                    Expanded(
                      child: Text(
                        product.description,
                        style: TextStyle(fontSize: 12.0, color: Colors.black.withOpacity(0.6)),
                      ),
                    ),
                    SizedBox(
                      height: 8.0,
                    ),
                    Container(
                      padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
                      decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(32.0),
                          color: Colors.teal
                      ),
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Text(
                            product.rating.rate.toString(),
                            style: TextStyle(
                                color: Colors.white,
                                fontSize: 16.0
                            ),
                            textAlign: TextAlign.center,
                          ),
                          SizedBox(width: 6.0,),
                          Icon(
                            FontAwesomeIcons.star,
                            color: Colors.white,
                            size: 20.0,
                          )
                        ],
                      ),
                    ),
                    SizedBox(height: 10.0,),
                    Text("Quantity: ", textAlign: TextAlign.start,),
                    Row(
                      children: [
                        GestureDetector(
                          child: Container(
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(12.0),
                              color: Colors.blue.withOpacity(0.5),
                            ),
                            child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Icon(FontAwesomeIcons.minus, color: Colors.white, size: 20.0,),
                            ),
                          ),
                          onTap: () => {},
                        ),
                        SizedBox(width: 8.0,),
                        GestureDetector(
                          child: Container(
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(12.0),
                              color: Colors.blue.withOpacity(0.5),
                            ),
                            child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Icon(FontAwesomeIcons.plus, color: Colors.white, size: 20.0,),
                            ),
                          ),
                          onTap: () => {},
                        ),
                      ],
                    ),
                    SizedBox(
                      height: 8.0,
                    ),
                    MaterialButton(
                      onPressed: () => {},
                      minWidth: double.infinity,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(32.0),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(12.0),
                        child: Text(
                          "Add to cart",
                          style: TextStyle(color: Colors.white),
                        ),
                      ),
                      color: Colors.blue,
                    )
                  ],
                ),
              ),
            ),
          )
        ],
      ),
    );
  }
}
