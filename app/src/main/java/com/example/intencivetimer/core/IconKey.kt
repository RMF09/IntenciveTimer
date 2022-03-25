package com.example.intencivetimer.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class IconKey(val rewardIcon: ImageVector) {
    STAR(Icons.Default.Star),
    CAKE(Icons.Default.Cake),
    BATH_TUB(Icons.Default.Bathtub),
    TV(Icons.Default.Tv),
    FAVORITE(Icons.Default.Favorite),
    PETS(Icons.Default.Pets),
    SMARTPHONE(Icons.Default.Smartphone),
    GAMEPAD(Icons.Default.Gamepad),
    CARD_GIFT_CARD(Icons.Default.CardGiftcard),
    MONEY(Icons.Default.Money),
    COMPUTER(Icons.Default.Computer),
    GROUP(Icons.Default.Group),
    SKATE_BOARDING(Icons.Default.Skateboarding),
    MOOD(Icons.Default.Mood),
    EMOJI_FOOD_BEVERAGE(Icons.Default.EmojiFoodBeverage),
    SPORT_MOTO(Icons.Default.SportsMotorsports),
    SPORT_FOOTBALL(Icons.Default.SportsFootball),
    SEARCH(Icons.Default.Search),
    HEADPHONES(Icons.Default.CardGiftcard),
    SHOPPING_CART(Icons.Default.ShoppingCart),
    DIRETCTION_BIKE(Icons.Default.DirectionsBike),
    LOCAL_PIZZA(Icons.Default.LocalPizza)
}

val defaultRewardIcon = IconKey.STAR