import { Component } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { ProfileService } from '../service/profile.service';
import { MyappService } from '../service/myapp.service';
import {
	FormGroup,
	FormBuilder,
	Validators,
	FormControl,
} from '@angular/forms';
import '../../assets/toast/main.js';
import { error } from 'jquery';
declare var toast: any;
@Component({
	selector: 'app-changepassword',
	templateUrl: './changepassword.component.html',
	styleUrls: ['./changepassword.component.css']
})
export class ChangepasswordComponent {
	userDisplayName = '';
	public changePasswordForm!: FormGroup;
	submitted: boolean = false;

	ngOnInit() {
		this.createFormChangePassword();
	}

	constructor(
		private cookieService: CookieService,
		private loginService: LoginService,
		private router: Router,
		private profileService: ProfileService,
		private formbuilder: FormBuilder,
		private myappService: MyappService
	) {
		this.userDisplayName = this.cookieService.get('full_name');
	}

	createFormChangePassword() {
		this.changePasswordForm = this.formbuilder.group({
			oldPass: [''],
			newPass: [''],
			reNewPass: ['']
		});
	}
	get profileFormControl() {
		return this.changePasswordForm.controls;
	}
	updatePass() {
		this.submitted = true;
		if (this.changePasswordForm.get("newPass").value == this.changePasswordForm.get("reNewPass").value) {
			this.profileService.updatePassoword(this.changePasswordForm.value).subscribe((res) => {
				if (res == "success") {
					new toast({
						title: 'Thành công!',
						message: 'Cập nhật thành công!',
						type: 'success',
						duration: 2000,
					});
				} else {
					new toast({
						title: 'Thất bại!',
						message: 'Mật khẩu cũ không trùng khớp!',
						type: 'warning',
						duration: 2000,
					});
				}

			})
		} else {
			new toast({
				title: 'Thất bại!',
				message: 'Vui lòng kiểm tra lại xác nhận mật khẩu!',
				type: 'error',
				duration: 2000,
			});
		}
	}
	// }

	showHideOldPass() {
		let input1 = document.getElementById('oldPass') as HTMLInputElement;
		// console.log(input!);
		if (input1.type === 'password') {
			input1.type = 'text';
			document.getElementById('eye1')!.className = 'fa-regular fa-eye';
		} else {
			input1.type = 'password';
			document.getElementById('eye1')!.className = 'fa-regular fa-eye-slash';
		}
	}

	showHideNewPass() {
		let input2 = document.getElementById('newPass') as HTMLInputElement;
		let input3 = document.getElementById('reNewPass') as HTMLInputElement;
		if (input2.type === 'password' || input2.type === 'password') {
			input2.type = 'text';
			input3.type = 'text';
			document.getElementById('eye2')!.className = 'fa-regular fa-eye';
			document.getElementById('eye3')!.className = 'fa-regular fa-eye';
		} else {
			input2.type = 'password';
			input3.type = 'password';
			document.getElementById('eye2')!.className = 'fa-regular fa-eye-slash';
			document.getElementById('eye3')!.className = 'fa-regular fa-eye-slash';
		}
	}

	isLogin() {
		return this.loginService.isLogin();
	}

	logout() {
		return this.loginService.logout();
	}

	loadDataProfile() {
		return this.myappService.loadDataProfile();
	}
}
