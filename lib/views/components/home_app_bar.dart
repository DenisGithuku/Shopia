import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class HomeAppBar extends StatelessWidget with PreferredSizeWidget {
  const HomeAppBar({Key? key, required this.username}) : super(key: key);

  final String username;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 8, horizontal: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(
            "Welcome $username"
          ),
          Container(
            padding: EdgeInsets.all(4.0),
            decoration: BoxDecoration(
                color: Colors.teal,
              borderRadius: BorderRadius.all(Radius.circular(12))
            ),
            height: 40,
            width: 40,
            child: Center(
              child: Icon(
                FontAwesomeIcons.cartShopping,
                color: Colors.white,
                size: 16,
              ),
            ),
          )
        ],
      ),
    );
  }

  @override
  // TODO: implement preferredSize
  Size get preferredSize => Size.fromHeight(kToolbarHeight);
}
