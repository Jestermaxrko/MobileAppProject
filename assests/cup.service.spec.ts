import { TestBed, inject } from '@angular/core/testing';

import { CupService } from './cup.service';

describe('CupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CupService]
    });
  });

  it('should ...', inject([CupService], (service: CupService) => {
    expect(service).toBeTruthy();
  }));
});
