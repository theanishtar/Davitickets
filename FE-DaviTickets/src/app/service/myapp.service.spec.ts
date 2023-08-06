import { TestBed } from '@angular/core/testing';

import { MyappService } from './myapp.service';

describe('MyappService', () => {
  let service: MyappService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MyappService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
