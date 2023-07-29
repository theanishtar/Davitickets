import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ListMovieBookingComponent } from './list-movie-booking/list-movie-booking.component';
import { BookingComponent } from './booking/booking.component';
import { PayComponent } from './pay/pay.component';
import { ChangepasswordComponent } from './changepassword/changepassword.component';
import { ForgotpasswordComponent } from './forgotpassword/forgotpassword.component';
import { ProfileComponent } from './profile/profile.component';
import { HistoryComponent } from './history/history.component';
import { authGuard } from './auth.guard';

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'listMovieBooking', component: ListMovieBookingComponent, canActivate: [authGuard]},
  {path: 'booking', component: BookingComponent, canActivate: [authGuard]},
  {path: 'pay', component: PayComponent, canActivate: [authGuard]},
  {path: 'changepass', component: ChangepasswordComponent, canActivate: [authGuard]},
  {path: 'forgotpass', component: ForgotpasswordComponent, canActivate: [authGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [authGuard]},
  {path: 'history', component: HistoryComponent, canActivate: [authGuard]},
  {path: 'listMovieBooking/:date', component: ListMovieBookingComponent, canActivate: [authGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
