package pl.javastart.couponcalc;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PriceCalculator {

    public double calculatePrice(List<Product> products, List<Coupon> coupons) {
        double totalPrice;
        if (products == null) {
            return 0;
        }
        totalPrice = totalProductsPrice(products);
        if (coupons == null) {
            return (double) Math.round(totalPrice * 100.0) / 100;
        }
        double bestPrice = totalPrice;
        int anyProductCouponBestValue = anyProductBestCoupon(coupons);
        if (anyProductCouponBestValue > 0) {
            bestPrice = totalPrice - (totalPrice * ((double) anyProductCouponBestValue / 100));
        }
        Map<Category, Double> productsPricePerCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));
        for (Map.Entry<Category, Double> categoryDoubleEntry : productsPricePerCategory.entrySet()) {
            int bestCoupon = specifiedCategoryBestCoupon(coupons, categoryDoubleEntry.getKey());
            double categoryTotalPrice = categoryDoubleEntry.getValue();
            double priceForThisCoupon = totalPrice - (categoryTotalPrice * ((double) bestCoupon / 100));
            bestPrice = Math.min(bestPrice, priceForThisCoupon);
        }
        return (double) Math.round(bestPrice * 100.0) / 100;
    }

    private int anyProductBestCoupon(List<Coupon> coupons) {
        TreeSet<Integer> couponValues = new TreeSet<>();
        if (coupons != null) {
            couponValues = findCouponsByCategory(coupons, null);
        }
        if (couponValues.isEmpty()) {
            return 0;
        }
        return couponValues.last();
    }

    private int specifiedCategoryBestCoupon(List<Coupon> coupons, Category category) {
        TreeSet<Integer> couponValues = new TreeSet<>();
        if (coupons != null) {
            couponValues = findCouponsByCategory(coupons, category);
        }
        if (couponValues.isEmpty()) {
            return 0;
        }
        return couponValues.last();
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
