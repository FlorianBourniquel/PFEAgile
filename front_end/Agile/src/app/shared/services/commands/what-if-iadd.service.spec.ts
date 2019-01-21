import { TestBed } from '@angular/core/testing';

import { WhatIfIAddService } from './what-if-iadd.service';

describe('WhatIfIAddService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WhatIfIAddService = TestBed.get(WhatIfIAddService);
    expect(service).toBeTruthy();
  });
});
