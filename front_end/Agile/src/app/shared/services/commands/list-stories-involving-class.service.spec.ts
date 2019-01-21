import { TestBed } from '@angular/core/testing';

import { ListStoriesInvolvingClassService } from './list-stories-involving-class.service';

describe('ListStoriesInvolvingClassService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ListStoriesInvolvingClassService = TestBed.get(ListStoriesInvolvingClassService);
    expect(service).toBeTruthy();
  });
});
