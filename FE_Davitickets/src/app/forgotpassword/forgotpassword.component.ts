import { ValidatorService } from './../service/validator.service';
import { Component } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { CookieService } from 'ngx-cookie-service';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import '../../assets/toast/main.js';
import { ForgotpasswordService } from './../service/forgotpassword.service';
declare var toast: any;
@Component({
  selector: 'app-forgotpassword',
  templateUrl: './forgotpassword.component.html',
  styleUrls: ['./forgotpassword.component.css'],
})
export class ForgotpasswordComponent {
  public checkForm1 = true;
  public checkForm2 = false;
  public checkForm3 = false;

  public emailForm!: FormGroup;
  public validCodeForm!: FormGroup;
  public changePassForm!: FormGroup;

  constructor(
    private formbuilder: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private location: Location,
    private cookieService: CookieService,
    private route: ActivatedRoute,
    private httpClient: HttpClient,
    private forgotpasswordService: ForgotpasswordService
  ) {
    this.createFormEmail();
    this.createFormValidCode();
    this.createFormChangePass();
  }

  createFormEmail() {
    this.emailForm = this.formbuilder.group({
      email: ['', Validators.required],
    });
  }
  get emailFormControl() {
    return this.emailForm.controls;
  }

  checkMailForgotPass() {
    if (this.emailForm.valid) {
      this.forgotpasswordService
        .sendMail(this.emailForm.value)
        .subscribe((res) => {
          if (res.sesionId == "errorusernull") {
            new toast({
              title: 'Thất bại!',
              message: 'Email không tồn tại!',
              type: 'error',
              duration: 1500,
            });
          } else if(res.sesionId == "success"){
            new toast({
              title: 'Thành công!',
              message: 'Mã của bạn đã được gửi!',
              type: 'success',
              duration: 1500,
            });
            this.checkForm1 = false;
            this.checkForm2 = true;
          }
        });
    }
  }

  createFormValidCode() {
    this.validCodeForm = this.formbuilder.group({
      validCode: ['', Validators.required],
    });
  }
  get validCodeFormControl() {
    return this.validCodeForm.controls;
  }

  checkValidCode() {
    if (this.validCodeForm.valid) {
      this.forgotpasswordService
        .checkCodeMail(this.validCodeForm.value)
        .subscribe((res) => {
          if ((res.sesionId = "success")) {
            this.checkForm2 = false;
            this.checkForm3 = true;
          } else if ((res.sesionId = "errorcode")) {
            new toast({
              title: 'Thất bại!',
              message: 'Mã của bạn đã sai!',
              type: 'error',
              duration: 1500,
            });
          } else {
            new toast({
              title: 'Thất bại!',
              message: 'Vui lòng nhập lại Email!',
              type: 'error',
              duration: 1500,
            });
            this.checkForm1 = true;
            this.checkForm2 = false;
            this.checkForm3 = false;
          }
        });
    }
  }

  createFormChangePass() {
    this.changePassForm = this.formbuilder.group({
      newPass: ['', Validators.required],
      reNewPass: ['', Validators.required],
    });
  }
  get changePassFormControl() {
    return this.changePassForm.controls;
  }

  changePass() {
    alert(1)
    // if(this.changePassForm.valid){
      this.forgotpasswordService.changePassword(this.changePassForm.value).subscribe((res)=>{
        function delay(ms: number) {
          return new Promise(function (resolve) {
            setTimeout(resolve, ms);
          });
        }
       alert(2)
          new toast({
            title: 'Thành công!',
            message: 'Cập nhật mật khẩu thành công!',
            type: 'success',
            duration: 1500,
          });
          delay(2000).then((res) => {
            this.router.navigate(['home']);
            delay(1).then((_) => {
              location.reload();
            });
          });
          this.checkForm1=true;
          this.checkForm2=false;
          this.checkForm3=false;

        
      })
    }
  }
// }
