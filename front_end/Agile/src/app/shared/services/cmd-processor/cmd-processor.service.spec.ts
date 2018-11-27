import { TestBed } from '@angular/core/testing';

import { CmdProcessorService } from './cmd-processor.service';

describe('CmdProcessorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CmdProcessorService = TestBed.get(CmdProcessorService);
    expect(service).toBeTruthy();
  });
});
