import { TestBed } from '@angular/core/testing';

import { ListMovieService } from './list-movie.service';

describe('ListMovieService', () => {
  let service: ListMovieService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListMovieService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
