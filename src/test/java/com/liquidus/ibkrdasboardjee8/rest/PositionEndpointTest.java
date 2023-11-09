package com.liquidus.ibkrdasboardjee8.rest;

import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;
import com.liquidus.ibkrdasboardjee8.entity.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionEndpointTest {

    @Mock
    private PositionLocal positionBean;


    @InjectMocks
    private PositionEndpoint positionEndpoint;

    @Test
    public void testCreatePosition() {
        // Mock the necessary dependencies
        Position position = mock(Position.class);

        // Call the method under test
        Response response = positionEndpoint.createPosition(position);

        // Verify the response
        assertThat(response.getStatus(), is(equalTo(Response.Status.CREATED.getStatusCode())));
        assertThat(response.getLocation(), is(notNullValue()));

        // Verify that the positionBean.addPosition() method was called
        verify(positionBean, times(1)).addPosition(position);
    }

    @Test
    public void testFindPositionByContractId() {
        Position position = mock(Position.class);

        // try null contract Id
        when(positionBean.findPositionById(eq(0))).thenReturn(null);
        Response responseWrongCid = positionEndpoint.findPositionByContractId(0);

        assertThat(responseWrongCid.getStatus(), is(equalTo(Response.Status.NOT_FOUND.getStatusCode())));

        // test with correct contract id
        when(positionBean.findPositionById(eq(45540822))).thenReturn(position);
        Response responseCorrect = positionEndpoint.findPositionByContractId(45540822);

        assertThat(responseCorrect.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }


    @Test
    public void testListAllPositions() {
        Response responseCorrect = positionEndpoint.listAllPositions(10, 10);
        assertThat(responseCorrect.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

        // returns default list with all the positions
        Response responseWrong = positionEndpoint.listAllPositions(null, null);
        assertThat(responseWrong.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    public void testUpdatePosition() {
        Position position = mock(Position.class);

        // try to find position with the wrong cid
        when(positionBean.findPositionById(eq(123))).thenReturn(null);
        // passed cid of the non-existing position
        Response responseWrong = positionEndpoint.updatePosition(123, position);
        assertThat(responseWrong.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        // try to find the position with the correct id
        when(positionBean.findPositionById(eq(45540822))).thenReturn(position);
        // passed cid of the existing position
        Response responseCorrect = positionEndpoint.updatePosition(45540822, position);
        assertThat(responseCorrect.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    public void testDeletePosition() {
        Position position = mock(Position.class);

        when(positionBean.findPositionById(123)).thenReturn(null);
        Response responseWrong = positionEndpoint.deletePositionByContractId(123);
        assertThat(responseWrong.getStatus(), is(equalTo(Response.Status.NOT_FOUND.getStatusCode())));

        when(positionBean.findPositionById(45540822)).thenReturn(position);
        Response responseCorrect = positionEndpoint.deletePositionByContractId(45540822);
        assertThat(responseCorrect.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }
}
