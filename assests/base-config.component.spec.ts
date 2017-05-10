import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseConfigComponent } from './base-config.component';

describe('BaseConfigComponent', () => {
  let component: BaseConfigComponent;
  let fixture: ComponentFixture<BaseConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BaseConfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
