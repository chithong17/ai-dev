import { test, expect } from '@playwright/test';

test.describe('Flow: Đặt vé Bước 1 (E2E)', () => {

  test.beforeEach(async ({ page }) => {
    // Đăng nhập trước
    await page.goto('http://localhost:3000/');
    await page.fill('input[name="empid"]', '10000006'); // User 7 (Sales)
    await page.fill('input[name="password"]', 'kxXRk7GIHw'); // Pass thật
    await page.click('button[type="submit"]');
    await page.waitForURL('http://localhost:3000/search');
    
    // Điều hướng đến trang đặt vé (Giả sử có 1 nút F6=SELL)
    // Vì không có nút, chúng ta điều hướng thủ công
    await page.goto('http://localhost:3000/booking-step1');
  });

  test('Xác thực Bước 1 (Research) thành công khớp với sell1-1.png', async ({ page }) => {
    // 1. Điền dữ liệu từ sell1-1.png [cite: "kerestes/cobol-airlines/COBOL-AIRLINES-04806ce49a9532da2a9404e0f384e32c5b1c9ccb/CICS/SALES-MAP/sell1-1.png"]
    await page.fill('input[name="clientid"]', '100'); // CLIENT ID
    await page.fill('input[name="flightnum"]', 'CB2204'); // FLIGHT NUM
    await page.fill('input[name="date"]', '2022-09-29'); // DATE
    await page.fill('input[name="passnum"]', '3'); // PASS NUMBER

    // 2. Nhấp nút "F11=RESEARCH"
    await page.click('button:text("F11=RESEARCH")');

    // 3. Khẳng định (Assert) các trường chỉ đọc (read-only) được điền
    // Dữ liệu này đến từ logic của BookingServiceImpl (dựa trên SELL1-COB [cite: "kerestes/cobol-airlines/COBOL-AIRLINES-04806ce49a9532da2a9404e0f384e32c5b1c9ccb/CICS/SALES-MAP/SELL1-COB", lines 283-294])
    
    // (Đợi cho phần tử .booking-quote xuất hiện)
    await page.waitForSelector('.booking-quote'); 
    
    const quoteElement = page.locator('.booking-quote');
    
    // Kiểm tra giá (logic COBOL: 120.99 * 3)
    await expect(quoteElement.locator('div:has-text("PRICE:")')).toHaveText('PRICE: 120.99');
    await expect(quoteElement.locator('div:has-text("TOTAL PRICE:")')).toHaveText('TOTAL PRICE: 362.97');
    
    // Kiểm tra thông tin chuyến bay
    await expect(quoteElement.locator('div:has-text("FLIGHT NUM:")')).toHaveText('FLIGHT NUM: CB2204');
    await expect(quoteElement.locator('div:has-text("DEP AIRPORT:")')).toHaveText('DEP AIRPORT: CDG');
    await expect(quoteElement.locator('div:has-text("LAND AIRPORT:")')).toHaveText('LAND AIRPORT: FCO');
  });

  test('Hiển thị lỗi COBOL gốc khi Khách hàng không tồn tại', async ({ page }) => {
    await page.fill('input[name="clientid"]', '999999'); // ID không tồn tại
    await page.fill('input[name="flightnum"]', 'CB2204');
    await page.fill('input[name="date"]', '2022-09-29');
    await page.fill('input[name="passnum"]', '1');

    await page.click('button:text("F11=RESEARCH")');

    // Khẳng định (Assert) thông báo lỗi từ SELL1-COB [cite: "kerestes/cobol-airlines/COBOL-AIRLINES-04806ce49a9532da2a9404e0f384e32c5b1c9ccb/CICS/SALES-MAP/SELL1-COB", line 249]
    const errorMessage = page.locator('.error-message');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText('THIS PASSENGER DOES NOT EXIST');
  });

});
