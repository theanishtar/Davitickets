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

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'listMovieBooking', component: ListMovieBookingComponent},
  {path: 'booking', component: BookingComponent},
  {path: 'pay', component: PayComponent},
  {path: 'changepass', component: ChangepasswordComponent},
  {path: 'forgotpass', component: ForgotpasswordComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'history', component: HistoryComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
