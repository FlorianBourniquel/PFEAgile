import { TestBed } from '@angular/core/testing';

import { WhatIfIRemoveService } from './what-if-iremove.service';

describe('WhatIfIRemoveService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WhatIfIRemoveService = TestBed.get(WhatIfIRemoveService);
    expect(service).toBeTruthy();
  });
});
