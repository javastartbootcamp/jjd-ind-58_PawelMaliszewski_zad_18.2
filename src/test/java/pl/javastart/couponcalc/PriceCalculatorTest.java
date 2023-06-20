package pl.javastart.couponcalc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceCalculatorTest {

    @Mock
    PriceCalculator priceCalculator;

    @BeforeEach
    public void init() {
        priceCalculator = new PriceCalculator();
    }

    @Test
    public void shouldReturnZeroForNoProducts() {

        // when
        double result = priceCalculator.calculatePrice(null, null);

        // then
        assertThat(result).isEqualTo(0.);
    }

    @Test
    public void shouldReturnPriceForSingleProductAndNoCoupons() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.99, Category.FOOD));

        // when
        double result = priceCalculator.calculatePrice(products, null);

        // then
        assertThat(result).isEqualTo(5.99);
    }

    @Test
    public void shouldReturnPriceForSingleProductAndOneCoupon() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.99, Category.FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(Category.FOOD, 20));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(4.79);
    }

    @Test
    public void shouldReturnPriceForSingleProductAndBestCoupon() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Amplifier", 7999, Category.ENTERTAINMENT));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(Category.ENTERTAINMENT, 50));
        coupons.add(new Coupon(Category.ENTERTAINMENT, 10));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(3999.5);
    }

    @Test
    public void shouldApplyOneCouponToAllProducts() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Amplifier", 70, Category.ENTERTAINMENT));
        products.add(new Product("Masło", 5, Category.HOME));
        products.add(new Product("Wycieraczki", 10, Category.CAR));
        products.add(new Product("EarPhones", 10, Category.ENTERTAINMENT));
        products.add(new Product("Chleb", 5, Category.FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(null, 10));
        coupons.add(new Coupon(null, 5));
        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(90);
    }

    @Test
    public void shouldApplyOneDiscountToOneCategory() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Amplifier", 2999, Category.ENTERTAINMENT));
        products.add(new Product("Masło", 5.99, Category.HOME));
        products.add(new Product("Wycieraczki", 150, Category.CAR));
        products.add(new Product("EarPhones", 100, Category.ENTERTAINMENT));
        products.add(new Product("Chleb", 4.58, Category.FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(Category.ENTERTAINMENT, 20));
        coupons.add(new Coupon(Category.ENTERTAINMENT, 5));
        coupons.add(new Coupon(Category.FOOD, 5));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(2639.77);
    }

    @Test
    public void shouldApplyFoodCouponOnly() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Chleb", 4.58, Category.FOOD));
        products.add(new Product("Mleko", 3.26, Category.FOOD));
        products.add(new Product("Ziemniaki", 10.55, Category.FOOD));
        products.add(new Product("Wędlina", 14.58, Category.FOOD));
        products.add(new Product("Woda", 15, Category.FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(Category.FOOD, 50));
        coupons.add(new Coupon(Category.FOOD, 10));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(23.99);
    }

    @Test
    public void shouldApplyBestCouponOptionDiscount() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Amplifier", 2999, Category.ENTERTAINMENT));
        products.add(new Product("Masło", 5.99, Category.HOME));
        products.add(new Product("Wycieraczki", 150, Category.CAR));
        products.add(new Product("EarPhones", 100, Category.ENTERTAINMENT));
        products.add(new Product("Chleb", 4.58, Category.FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(Category.ENTERTAINMENT, 20));
        coupons.add(new Coupon(Category.ENTERTAINMENT, 5));
        coupons.add(new Coupon(Category.FOOD, 5));
        coupons.add(new Coupon(null, 20));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(2607.66);
    }

    @Test
    public void shouldReturnPriceForNoCoupons() {

        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Amplifier", 2999, Category.ENTERTAINMENT));
        products.add(new Product("Masło", 5.99, Category.HOME));
        products.add(new Product("Wycieraczki", 150, Category.CAR));
        products.add(new Product("EarPhones", 100, Category.ENTERTAINMENT));
        products.add(new Product("Chleb", 4.58, Category.FOOD));

        // when
        double result = priceCalculator.calculatePrice(products, null);

        // then
        assertThat(result).isEqualTo(3259.57);
    }


}