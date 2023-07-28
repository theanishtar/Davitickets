import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ValidatorFn, AbstractControl } from '@angular/forms';
import { catchError, of, tap } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class ValidatorService {
 
  emailValidator = (): ValidatorFn => {
    return (control: AbstractControl): { [key: string]: string } => {
      const result = /^[\\w\\.=-]+@[\\w\\.-]+\\.[\\w]{2,3}$/.test(
        control.value
      );
      return result == true ? null! : { error: 'wrong email format' };
    };
  };

  // phonelValidator = (): ValidatorFn => {
  //   return (control: AbstractControl): {[key: string]: string} => {
  //     const result = (/(84[3|5|7|8|9])+([0-9]{8})\b/g).test(control.value);
  //     return result == true ? null! : {error: "wrong phone format"};
  //     // return result ? null : { invalidPassword: true };
  //   };
  // }

  phonelValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (!control.value) {
        return null;
      }
      const regex = new RegExp('^(0d{9,10})$');
      const valid = regex.test(control.value);
      return valid ? null : { invalidPhone: true };
    };
  }

  constructor(private http: HttpClient) {}
}
