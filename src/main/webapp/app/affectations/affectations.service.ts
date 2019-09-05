import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAffectations } from 'app/shared/model/affectations.model';

type EntityResponseType = HttpResponse<IAffectations>;
type EntityArrayResponseType = HttpResponse<IAffectations[]>;

@Injectable({ providedIn: 'root' })
export class AffectationsService {
    public resourceUrl = SERVER_API_URL + 'api/affectations';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/affectations';

    constructor(protected http: HttpClient) {}

    create(affectations: IAffectations): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(affectations);
        return this.http
            .post<IAffectations>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(affectations: IAffectations): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(affectations);
        return this.http
            .put<IAffectations>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAffectations>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAffectations[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAffectations[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(affectations: IAffectations): IAffectations {
        const copy: IAffectations = Object.assign({}, affectations, {
            dateDebut:
                affectations.dateDebut != null && affectations.dateDebut.isValid() ? affectations.dateDebut.format(DATE_FORMAT) : null,
            dateFin: affectations.dateFin != null && affectations.dateFin.isValid() ? affectations.dateFin.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateDebut = res.body.dateDebut != null ? moment(res.body.dateDebut) : null;
            res.body.dateFin = res.body.dateFin != null ? moment(res.body.dateFin) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((affectations: IAffectations) => {
                affectations.dateDebut = affectations.dateDebut != null ? moment(affectations.dateDebut) : null;
                affectations.dateFin = affectations.dateFin != null ? moment(affectations.dateFin) : null;
            });
        }
        return res;
    }
}
