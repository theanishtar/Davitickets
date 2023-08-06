import { Component } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';
@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent {
  isComponentVisible: boolean = false;
  check: boolean = false;
  constructor(private route: ActivatedRoute, private router: Router, private location: Location) {}

  ngOnInit(): void {
    // Kiểm tra xem component có đang hiển thị hay không khi khởi tạo
    this.checkVisibility();

    // Theo dõi sự kiện thay đổi URL để kiểm tra lại khi người dùng chuyển trang
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.checkVisibility();
        this.check = false;
      }
    });
  }

  checkVisibility(): void {
    const header: HTMLElement | null = document.querySelector('.site-header');
    const footer: HTMLElement | null = document.querySelector('.site-footer');
    // Kiểm tra xem có route của component đang active hay không
    this.isComponentVisible = this.route.snapshot.routeConfig?.path === '**';
    this.check = true
    if(this.check == true){
      header.style.display = 'none'
      footer.style.display = 'none'
    }
  }

  back() {
    this.location.back(); // <-- go back to previous location on cancel
  }
}
