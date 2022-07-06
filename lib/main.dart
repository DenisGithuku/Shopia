import 'package:flutter/material.dart';
import 'package:flutter_shop/views/screens/home.dart';
import 'package:get/get.dart';

void main() => runApp(FakeStoreApp());

class FakeStoreApp extends StatelessWidget {
  const FakeStoreApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      debugShowCheckedModeBanner: false,
      home: Home(),
    );
  }
}
