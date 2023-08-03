// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { of } from 'rxjs';
// import { catchError, map, tap } from 'rxjs/operators';
// @Injectable({
//   providedIn: 'root'
// })
// export class ListMovieService {
//   private selectedDate: Date;
//   private apiUrl = `http://localhost:3000/users?release_date=${this.selectedDate}`;

//   listMovieByDate() {
//     // return this.http.post(this.userURL, data);
//     return this.http.get<any>(this.apiUrl).pipe(
//           // tap(() => console.log("Lấy dữ liệu thành công")),
//           tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
//           catchError(error => of([alert(error)]))
//         )
//   }
//   constructor(private http: HttpClient,) { }
// }
