import { TestBed } from '@angular/core/testing';

import { RemoveFromBacklogService } from './remove-from-backlog.service';

describe('RemoveFromBacklogService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RemoveFromBacklogService = TestBed.get(RemoveFromBacklogService);
    expect(service).toBeTruthy();
  });
});
