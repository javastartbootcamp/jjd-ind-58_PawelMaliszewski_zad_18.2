package pl.javastart.couponcalc;

import java.util.*;
import java.util.stream.Collectors;

public class PriceCalculator {

    public double calculatePrice(List<Product> products, List<Coupon> coupons) {
        double totalPrice = 0;
        if (products != null) {
            totalPrice = totalProductsPrice(products);
            if (coupons != null) {
                TreeSet<Double> couponPrices = new TreeSet<>();
                int anyProductCouponBestValue = anyProductBestCoupon(coupons);
                if (anyProductCouponBestValue > 0) {
                    couponPrices.add(totalPrice - (totalPrice * ((double) anyProductCouponBestValue / 100)));
                }
                Set<Category> categories = checkHowManyProductCategory(products);
                for (Category category : categories) {
                    int bestCoupon = specifiedCategoryBestCoupon(coupons, category);
                    double categoryTotalPrice = getTotalPriceForCategory(products, category);
                    couponPrices.add(totalPrice - (categoryTotalPrice * ((double) bestCoupon / 100)));
                }
                totalPrice = couponPrices.first();
            }
        }
        String price = String.format("%.2f", totalPrice);
        price = price.replace(",", ".");
        return Double.parseDouble(price);
    }

    private Set<Category> checkHowManyProductCategory(List<Product> products) {
        return products.stream()
                .map(Product::getCategory)
                .collect(Collectors.toSet());
    }

    private double getTotalPriceForCategory(List<Product> products, Category category) {
        return products.stream()
                .filter(x -> x.getCategory().equals(category))
                .map(Product::getPrice)
                .reduce(0.0, Double::sum);
    }

    private int anyProductBestCoupon(List<Coupon> coupons) {
        TreeSet<Integer> cuponValues = new TreeSet<>();
        if (coupons != null) {
            cuponValues = findCouponsByCategory(coupons, null);
        }
        if (cuponValues.isEmpty()) {
            return 0;
        }
        return cuponValues.last();
    }

    private int specifiedCategoryBestCoupon(List<Coupon> coupons, Category category) {
        TreeSet<Integer> cuponValues = new TreeSet<>();
        if (coupons != null) {
            cuponValues = findCouponsByCategory(coupons, category);
        }
        if (cuponValues.isEmpty()) {
            return 0;
        }
        return cuponValues.last();
    }

    private TreeSet<Integer> findCouponsByCategory(List<Coupon> coupons, Category category) {
        return coupons.stream()
                .filter(x -> x.getCategory() == category)
                .map(Coupon::getDiscountValueInPercents)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private double totalProductsPrice(List<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(0.0, Double::sum);
    }
}
