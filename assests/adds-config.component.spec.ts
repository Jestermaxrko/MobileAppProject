import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddsConfigComponent } from './adds-config.component';

describe('AddsConfigComponent', () => {
  let component: AddsConfigComponent;
  let fixture: ComponentFixture<AddsConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddsConfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddsConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
