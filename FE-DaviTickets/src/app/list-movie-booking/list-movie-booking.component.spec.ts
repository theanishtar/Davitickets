import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListMovieBookingComponent } from './list-movie-booking.component';

describe('ListMovieBookingComponent', () => {
  let component: ListMovieBookingComponent;
  let fixture: ComponentFixture<ListMovieBookingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListMovieBookingComponent]
    });
    fixture = TestBed.createComponent(ListMovieBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
