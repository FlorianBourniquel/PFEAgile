import { TestBed } from '@angular/core/testing';

import { CreateSprintService } from './create-sprint.service';

describe('CreateSprintService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CreateSprintService = TestBed.get(CreateSprintService);
    expect(service).toBeTruthy();
  });
});
