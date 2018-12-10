import { TestBed } from '@angular/core/testing';

import {MoveStoryToNextSprintServiceService} from './move_story_to_next_sprint.service';

describe('MoveStoryToNextSprintServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MoveStoryToNextSprintServiceService = TestBed.get(MoveStoryToNextSprintServiceService);
    expect(service).toBeTruthy();
  });
});
