import { TestBed } from '@angular/core/testing';

import {InitBacklogService} from './init-backlog.service';

describe('InitBacklogService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: InitBacklogService = TestBed.get(InitBacklogService);
    expect(service).toBeTruthy();
  });
});
