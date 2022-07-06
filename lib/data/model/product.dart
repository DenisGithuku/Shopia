/// id : 1
/// title : "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops"
/// price : 109.95
/// description : "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday"
/// category : "men's clothing"
/// image : "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"
/// rating : {"rate":3.9,"count":120}

class Product {
  Product({
    required num id,
    required String title,
    required num price,
    required String description,
    required String category,
    required String image,
    required Rating rating,}) {
    _id = id;
    _title = title;
    _price = price;
    _description = description;
    _category = category;
    _image = image;
    _rating = rating;
  }

  Product.fromJson(dynamic json) {
    _id = json['id'];
    _title = json['title'];
    _price = json['price'];
    _description = json['description'];
    _category = json['category'];
    _image = json['image'];
    _rating =
    (json['rating'] != null ? Rating.fromJson(json['rating']) : null)!;
  }

  late num _id;
  late String _title;
  late num _price;
  late String _description;
  late String _category;
  late String _image;
  late Rating _rating;

  Product copyWith({
    required num id,
    required String title,
    required num price,
    required String description,
    required String category,
    required String image,
    required Rating rating,
  }) =>
      Product(id: id ?? _id,
        title: title ?? _title,
        price: price ?? _price,
        description: description ?? _description,
        category: category ?? _category,
        image: image ?? _image,
        rating: rating ?? _rating,
      );

  num get id => _id;

  String get title => _title;

  num get price => _price;

  String get description => _description;

  String get category => _category;

  String get image => _image;

  Rating get rating => _rating;

  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['id'] = _id;
    map['title'] = _title;
    map['price'] = _price;
    map['description'] = _description;
    map['category'] = _category;
    map['image'] = _image;
    if (_rating != null) {
      map['rating'] = _rating.toJson();
    }
    return map;
  }

}

/// rate : 3.9
/// count : 120

class Rating {
  Rating({
    required num rate,
    required num count,}) {
    final _rate = rate;
    final _count = count;
  }

  Rating.fromJson(dynamic json) {
    _rate = json['rate'];
    _count = json['count'];
  }

  late num _rate;
  late num _count;

  Rating copyWith({ required double rate,
    required num count,
  }) =>
      Rating(rate: rate ?? _rate,
        count: count ?? _count,
      );

  num get rate => _rate;

  num get count => _count;

  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['rate'] = _rate;
    map['count'] = _count;
    return map;
  }

}
