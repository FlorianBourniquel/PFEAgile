import { TestBed } from '@angular/core/testing';

import { ListBacklogService } from './list-backlog.service';

describe('ListBacklogService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ListBacklogService = TestBed.get(ListBacklogService);
    expect(service).toBeTruthy();
  });
});
