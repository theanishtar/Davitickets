import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { LoginService } from '../service/login.service';
import { delay, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import { ValidatorService } from '../service/validator.service';

//Xử lí bất đồng bộ
import { Observable } from 'rxjs';
import { of } from 'rxjs';

import Swal from 'sweetalert2';
import '../../assets/toast/main.js';
import { data, error } from 'jquery';
declare var toast: any;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  public profileForm!: FormGroup;
  userDisplayName = '';

  constructor(
    private formbuilder: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private location: Location,
    public loginService: LoginService,
    private cookieService: CookieService,
    private validatorService: ValidatorService,
  ){
    this.userDisplayName = this.cookieService.get('full_name'); 
  }

  createFormProfile() {
    this.profileForm = this.formbuilder.group({
      profile_picture: [''],
      full_name: [''],
      email: ['', [Validators.required, Validators.email, this.validatorService.emailValidator()]],
      phone: [],
      password: ['', Validators.required],
      birthday: [''],
      gender: [],
    });
  }
  get profileFormControl() {
    return this.profileForm.controls;
  }

  updateProfile(){
    
  }

  isLogin(){
    return this.loginService.isLogin();
  }

  logout(){
    return this.loginService.logout();
  }
}
