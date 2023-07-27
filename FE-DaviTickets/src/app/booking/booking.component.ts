import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent {
  ngOnInit(): void {
    const container = document.querySelector(".container-booking");
    // const seats = document.querySelectorAll(".row .seat:not(.occupied)");
    // const count = document.getElementById("count");
    // const total = document.getElementById("total");
    // const movieSelect = document.getElementById("movie");

    container.addEventListener("click", (e) => {
      const target = e.target as HTMLTextAreaElement;
      if (
        target.classList.contains("seat") &&
        !target.classList.contains("occupied") &&
        !target.classList.contains("notselected")
      ) {
        console.log("abc")
        target.classList.toggle("selected");
  
      }
    });
  }
}
