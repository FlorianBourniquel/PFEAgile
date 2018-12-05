import { TestBed } from '@angular/core/testing';

import { ChangeStatusService } from './change-status.service';

describe('ChangeStatusService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ChangeStatusService = TestBed.get(ChangeStatusService);
    expect(service).toBeTruthy();
  });
});
