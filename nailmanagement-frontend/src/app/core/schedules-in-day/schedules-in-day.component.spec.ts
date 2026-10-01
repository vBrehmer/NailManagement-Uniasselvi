import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedulesInDayComponent } from './schedules-in-day.component';

describe('SchedulesInDayComponent', () => {
  let component: SchedulesInDayComponent;
  let fixture: ComponentFixture<SchedulesInDayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchedulesInDayComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchedulesInDayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
