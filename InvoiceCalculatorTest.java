package InvoiceGenerator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InvoiceCalculatorTest {

    @Test
    public void testItemTotal() {
        double result = InvoiceCalculator.calculateItemTotal(2, 100);
        assertEquals(200.0, result, 0.01);
    }

    @Test
    public void testTaxCalculation() {
        double result = InvoiceCalculator.calculateTax(200);
        assertEquals(20.0, result, 0.01);
    }

    @Test
    public void testDiscountCalculation() {
        double result = InvoiceCalculator.calculateDiscount(200);
        assertEquals(10.0, result, 0.01);
    }

    @Test
    public void testGrandTotal() {
        double result = InvoiceCalculator.calculateGrandTotal(200, true, true);
        assertEquals(210.0, result, 0.01);
    }
}