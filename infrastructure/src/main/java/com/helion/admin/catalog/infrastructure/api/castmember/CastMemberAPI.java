package com.helion.admin.catalog.infrastructure.api.castmember;

import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.helion.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.helion.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.helion.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "cast_members")
@Tag(name = "Cast Members")
public interface CastMemberAPI {


    @Operation(summary = "Create a new cast member")
    @ApiResponses(value= {
            @ApiResponse(responseCode= "201", description = "Created successfully"),
            @ApiResponse(responseCode= "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode= "500", description = "An internal server error was thrown"),
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createCastMember(@RequestBody CreateCastMemberRequest input);

    @Operation(summary = "List all cast members paginated")
    @ApiResponses(value= {
            @ApiResponse(responseCode= "200", description = "Listed successfully"),
            @ApiResponse(responseCode= "422", description = "An invalid parameter was received"),
            @ApiResponse(responseCode= "500", description = "An internal server error was thrown"),
    })
    @GetMapping
    Pagination<CastMemberListResponse> listCastMembers(
            @RequestParam(name="search", required= false, defaultValue= "") final String search,
            @RequestParam(name="page", required= false, defaultValue= "0") final int page,
            @RequestParam(name="perPage", required= false, defaultValue= "10") final int perPage,
            @RequestParam(name="sort", required= false, defaultValue= "name") final String sort,
            @RequestParam(name="dir", required= false, defaultValue= "asc") final String direction

    );

    @GetMapping(
            value ="{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a cast member by it's identifier")
    @ApiResponses(value= {
            @ApiResponse(responseCode= "200", description = "Cast Member retrieved successfully"),
            @ApiResponse(responseCode= "404", description = "Cast Member was not found"),
            @ApiResponse(responseCode= "500", description = "An internal server error was thrown"),
    })
    CastMemberResponse getById(@PathVariable(name="id") String id);


    @PutMapping(
            value ="{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a cast member by it's identifier")
    @ApiResponses(value= {
            @ApiResponse(responseCode= "200", description = "Cast Member updated successfully"),
            @ApiResponse(responseCode= "404", description = "Cast Member was not found"),
            @ApiResponse(responseCode= "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable(name="id") String id, @RequestBody UpdateCastMemberRequest input);

    @DeleteMapping(
            value ="{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a cast member by it's identifier")

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value= {
            @ApiResponse(responseCode= "204", description = "Cast Member deleted successfully"),
            @ApiResponse(responseCode= "404", description = "Cast Member was not found"),
            @ApiResponse(responseCode= "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name="id") String id);
}
