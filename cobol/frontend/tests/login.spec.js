import { test, expect } from '@playwright/test';

test.describe('COBOL Airlines E2E', () => {
  
  test('Đăng nhập thất bại với mật khẩu sai', async ({ page }) => {
    await page.goto('http://localhost:3000/'); // URL của frontend
    
    // Sử dụng dữ liệu test thật từ Emplo-file
    await page.fill('input[name="empid"]', '10000006'); // User 7 (Sales)
    await page.fill('input[name="password"]', 'wrongpassword');
    
    await page.click('button[type="submit"]');
    
    // Khẳng định (Assert) thông báo lỗi COBOL gốc
    // (Chúng ta cần thêm class "error-message" vào component LoginPage)
    const errorMessage = page.locator('.error-message'); 
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText('PASSWORD OR USERID INCORRECT.');
  });

  test('Đăng nhập thành công và điều hướng (Sales Dept)', async ({ page }) => {
    await page.goto('http://localhost:3000/');
    
    // Dữ liệu thật từ Emplo-file
    await page.fill('input[name="empid"]', '10000006'); // User 7 (Sales)
    await page.fill('input[name="password"]', 'kxXRk7GIHw'); // Pass thật
    
    await page.click('button[type="submit"]');
    
    // Khẳng định điều hướng đến trang tìm kiếm (logic từ LOGIN-COB)
    await expect(page).toHaveURL('http://localhost:3000/search');
    await expect(page.locator('h2')).toHaveText('Search Flights');
  });

});
