import { test, expect } from '@playwright/test';

// Kịch bản test E2E cho Tìm kiếm Chuyến bay
// (Giả định chúng ta cần đăng nhập trước)
test.describe('Flow: Tìm kiếm Chuyến bay (E2E)', () => {

  // Helper: Đăng nhập trước mỗi bài test trong nhóm này
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:3000/');
    // Dữ liệu thật từ Emplo-file [cite: "kerestes/cobol-airlines/COBOL-AIRLINES-04806ce49a9532da2a9404e0f384e32c5b1c9ccb/AS-400/Insert/Emplo-file"]
    await page.fill('input[name="empid"]', '10000006'); // User 7 (Sales)
    await page.fill('input[name="password"]', 'kxXRk7GIHw'); // Pass thật
    await page.click('button[type="submit"]');
    // Đợi điều hướng hoàn tất
    await page.waitForURL('http://localhost:3000/search');
  });

  test('Tìm kiếm thành công (Date + Dep + Arr) khớp với SRCHFLY3.png', async ({ page }) => {
    // 1. Xác minh UI khớp với SRCHFLY1.png (trống) [cite: "kerestes/cobol-airlines/COBOL-AIRLINES-04806ce49a9532da2a9404e0f384e32c5b1c9ccb/CICS/SALES-MAP/SRCHFLY1.png"]
    await expect(page.locator('h2')).toHaveText('Search Flights');
    await expect(page.locator('label[for="fdate-input"]')).toHaveText('DATE:');
    await expect(page.locator('label[for="dep-input"]')).toHaveText('DEP AIRPORT:');

    // 2. Điền dữ liệu (từ SRCHFLY3.png [cite: "kerestes/cobol-airlines/COBOL-AIRLINES-04806ce49a9532da2a9404e0f384e32c5b1c9ccb/CICS/SALES-MAP/SRCHFLY3.png"])
    await page.fill('input[name="fdate"]', '2022-09-24');
    await page.fill('input[name="dep"]', 'LHR');
    await page.fill('input[name="arr"]', 'CDG');
    
    // 3. Gửi
    await page.click('button[type="submit"]');

    // 4. Khẳng định (Assert) kết quả khớp với SRCHFLY3.png
    const resultRow = page.locator('table > tbody > tr').first();
    await expect(resultRow.locator('td').nth(0)).toHaveText('CB4405'); // FID
    await expect(resultRow.locator('td').nth(1)).toHaveText('17:00'); // TDEP
    await expect(resultRow.locator('td').nth(3)).toHaveText('LHR');    // DEP
    await expect(resultRow.locator('td').nth(4)).toHaveText('CDG');    // LAND
    await expect(resultRow.locator('td').nth(6)).toHaveText('2022-09-24'); // DATE
  });

});
