import { TestBed } from '@angular/core/testing';

import { RemoveStoryService } from './remove-story.service';

describe('RemoveStoryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RemoveStoryService = TestBed.get(RemoveStoryService);
    expect(service).toBeTruthy();
  });
});
