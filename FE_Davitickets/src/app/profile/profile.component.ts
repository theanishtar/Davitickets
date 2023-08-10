import { Component, ElementRef, ViewChild, HostListener } from '@angular/core';
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
import { ProfileService } from '../service/profile.service';
import { delay, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import { ValidatorService } from '../service/validator.service';
import { MyappService } from '../service/myapp.service';
//up ảnh firebase
import {
  Storage,
  ref,
  uploadBytesResumable,
  getDownloadURL,
} from '@angular/fire/storage';
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
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent {
  @ViewChild('uploadPreview') uploadPreview: ElementRef;
  public profileForm!: FormGroup;
  userDisplayName = '';
  userEmail = '';
  profile: any[] = [];
  avatar = '';
  submitted: boolean = false;

  ngOnInit() {
    this.createFormProfile();
  }

  constructor(
    private formbuilder: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private location: Location,
    public loginService: LoginService,
    private cookieService: CookieService,
    private validatorService: ValidatorService,
    public storage: Storage,
    private profileService: ProfileService,
    private myappService: MyappService
  ) {
    this.userDisplayName = this.cookieService.get('full_name');
    this.userEmail = this.cookieService.get('userEmail');

    this.loadDataProfile();

    // if(this.cookieService.get("check") != null){
    //   this.cookieService.set("check", null);
    //   this.router.navigate(['home']);
    // }
    this.profile = JSON.parse(
      '[' + JSON.stringify(this.myappService.getProfile()) + ']'
    );
    console.log(this.profile);
  }

  loadDataProfile() {
    return this.myappService.loadDataProfile();
  }

  createFormProfile() {
    this.profileForm = this.formbuilder.group({
      fullname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      pic: [''],
      phone: ['', Validators.required],
      birtday: [''],
      password: [''],
      gender: [''],
    });
  }
  get profileFormControl() {
    return this.profileForm.controls;
  }

  updateProfile(): void {
    this.submitted = true;
    if (this.profileForm.valid) {
      var data = {
        fullname: this.profileForm.get('fullname').value,
        email: this.profileForm.get('email').value,
        pic: this.profileForm.get('pic').value,
        phone: this.profileForm.get('phone').value,
        birtday: this.profileForm.get('birtday').value,
        password: this.profileForm.get('password').value,
        gender: this.profileForm.get('gender').value,
      };
      this.profileService.updateProfile(data).subscribe((res) => {
        new toast({
          title: 'Thành công!',
          message: 'Cập nhật thành công!',
          type: 'success',
          duration: 2000,
        });
        this.cookieService.set(
          'full_name',
          this.profileForm.get('fullname').value
        );
        this.profile = JSON.parse(JSON.stringify(res));
      });
    }
  }

  chooseFile(event: any) {
    this.file = event.target.files[0];
    var oFReader = new FileReader();
    oFReader.readAsDataURL(this.file);

    oFReader.onload = (oFREvent: any) => {
      if (this.uploadPreview && this.uploadPreview.nativeElement) {
        this.uploadPreview.nativeElement.src = oFREvent.target.result;
      }
    };
    this.addData();
  }
  addData() {
    const storageRef = ref(this.storage, this.file.name);
    const uploadTast = uploadBytesResumable(storageRef, this.file);
    uploadTast.on(
      'state_changed',
      (snapshot) => {},
      (error) => {
        console.log(error.message);
      },
      () => {
        getDownloadURL(uploadTast.snapshot.ref).then((downloadURL) => {
          let timerInterval;
          Swal.fire({
            title: 'Upload!',
            html: 'Quá trình upload sẽ diễn ra trong vài giây!',
            timer: 2000,
            timerProgressBar: true,
            didOpen: () => {
              Swal.showLoading();
            },
            willClose: () => {
              clearInterval(timerInterval);
            },
          }).then((result) => {
            if (result.dismiss === Swal.DismissReason.timer) {
              console.log('I was closed by the timer');
            }
          });

          var x = downloadURL;
          console.log('URL', x);
          this.avatar = downloadURL;
          // document.getElementById('avatar').innerHTML = x;
        });
      }
    );
  }

  public file: any = {};

  isLogin() {
    return this.loginService.isLogin();
  }

  logout() {
    return this.loginService.logout();
  }

  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: any): void {
    // Display a confirmation message when the user tries to reload the page
    $event.returnValue = false;
  }
}
