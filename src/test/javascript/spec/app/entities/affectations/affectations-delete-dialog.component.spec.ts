/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChargeplanApplicationTestModule } from '../../../test.module';
import { AffectationsDeleteDialogComponent } from 'app/affectations/affectations-delete-dialog.component';
import { AffectationsService } from 'app/affectations/affectations.service';

describe('Component Tests', () => {
    describe('Affectations Management Delete Component', () => {
        let comp: AffectationsDeleteDialogComponent;
        let fixture: ComponentFixture<AffectationsDeleteDialogComponent>;
        let service: AffectationsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ChargeplanApplicationTestModule],
                declarations: [AffectationsDeleteDialogComponent]
            })
                .overrideTemplate(AffectationsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AffectationsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AffectationsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
