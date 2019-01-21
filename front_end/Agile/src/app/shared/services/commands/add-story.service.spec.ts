import { TestBed } from '@angular/core/testing';

import { AddStoryService } from './add-story.service';

describe('AddStoryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AddStoryService = TestBed.get(AddStoryService);
    expect(service).toBeTruthy();
  });
});
