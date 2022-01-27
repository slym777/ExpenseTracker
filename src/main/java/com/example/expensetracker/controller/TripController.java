package com.example.expensetracker.controller;

import com.example.expensetracker.dtos.CreateTripRequest;
import com.example.expensetracker.dtos.ExpenseDto;
import com.example.expensetracker.dtos.TripDto;
import com.example.expensetracker.model.Trip;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/allTrips")
    public ResponseEntity<?> getAllTrips(){ return ResponseEntity.ok().body(tripService.getAllTrips()); }

    @GetMapping("/{tripId}")
    public ResponseEntity<?> getTripById(@PathVariable Long tripId){ return ResponseEntity.ok().body(tripService.getTripById(tripId)); }

    @GetMapping("/userId={userId}")
    public ResponseEntity<?> getTripsByUserId(@PathVariable Long userId){ return ResponseEntity.ok().body(tripService.getTripsByUserId(userId)); }

    @PostMapping("/saveTrip")
    public ResponseEntity<?> SaveTrip(@RequestBody CreateTripRequest trip) {
        tripService.SaveTrip(trip);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/addMember/{tripId}")
    public ResponseEntity<?> AddMemberToTrip(@PathVariable Long tripId, @RequestBody User user) {return ResponseEntity.ok().body(tripService.AddMember(tripId, user));}

    @PostMapping("/addExpense/{tripId}")
    public ResponseEntity<?> AddExpenseToTrip(@PathVariable Long tripId, @RequestBody ExpenseDto expenseDto) {return ResponseEntity.ok().body(tripService.AddExpense(tripId, expenseDto));}

//    @GetMapping("/userHubs")
//    public List<Hub> getUserHubs(@CurrentUser UserPrincipal userPrincipal){
//        User user = userRepository.findUserById(userPrincipal.getId()).orElseThrow(
//                () -> new ResourceNotFoundException("User", "userId", userPrincipal.getId()));
//        return user.getHubs().stream()
//                .sorted(Comparator.comparing(Hub::getName))
//                .collect(Collectors.toList());
//    }

//    @GetMapping("/hubUsers/{hubId}")
//    public List<FriendResponse> getUsersOfHub(@PathVariable Long hubId, @CurrentUser UserPrincipal currentUser){
//        Hub hub = hubRepository.findGroupById(hubId).orElseThrow(
//                () -> new ResourceNotFoundException("Hub", "hubId", hubId));
//        User user = userRepository.findUserById(currentUser.getId()).orElseThrow(
//                () -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
//        return hub.getUsers().stream()
//                .filter(u -> !u.getId().equals(currentUser.getId()))
//                .map(u -> mapUserToHubFriendResponse(u, user, hubId))
//                .sorted(Comparator.comparing(FriendResponse::getUsername))
//                .collect(Collectors.toList());
//    }
//
//    @PostMapping("/createTrip")
//    public void createHub(@RequestBody CreateHubRequest createHubRequest, @CurrentUser UserPrincipal userPrincipal){
//        Hub hub = new Hub(createHubRequest.getName(), createHubRequest.getDescription(), userPrincipal.getId(), "https://firebasestorage.googleapis.com/v0/b/pocket-management.appspot.com/o/avatars%2Fcdb893c6-fd7f-4257-a9c5-92ea47aa5362.png?alt=media&token=4ffc1651-088e-4264-8924-b96948f6e4b2", createHubRequest.getCurrencyName());
//        User user = userRepository.findUserById(userPrincipal.getId()).orElseThrow(
//                () -> new ResourceNotFoundException("User", "userId", userPrincipal.getId()));
//        hub.addUser(user);
//        hubRepository.save(hub);
//
//        actionService.createHubAction(user.getUsername(), hub.getName(), hub.getId());
//    }
//
//    @PostMapping("/addSingleFriendToHub/{hubId}/{friendUsername}")
//    public void addSingleFriendToHub(@PathVariable String friendUsername, @PathVariable Long hubId){
//        Hub hub = hubRepository.findGroupById(hubId).orElseThrow(
//                () -> new ResourceNotFoundException("Hub", "hubId", hubId));
//        User user = userRepository.findByUsername(friendUsername).orElseThrow(
//                () -> new ResourceNotFoundException("User", "username", friendUsername));
//        hub.addUser(user);
//        hubRepository.save(hub);
//        userRepository.save(user);
//
//        actionService.createAddUserByQRCodeAction(friendUsername, hubId);
//    }
//
//    @PostMapping("/addListOfFriendsToHub/{hubId}")
//    public void addListOfFriendsToHub(@RequestBody List<String> friendsUsername, @PathVariable Long hubId, @CurrentUser UserPrincipal userPrincipal){
//        Hub hub = hubRepository.findGroupById(hubId).orElseThrow(
//                () -> new ResourceNotFoundException("Hub", "hubId", hubId));
//        List<Long> friendsIds = hub.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList());
//        friendsUsername.forEach(str -> {
//            addFriendToHub(hub, str);
//            actionService.createAddUserAction(str, hubId, userPrincipal.getUsername());
//        });
//        String message;
//        if (friendsIds.size() > 1)
//            message = hub.getName() + " | " + friendsUsername.size() + " were added to the hub";
//        else
//            message = hub.getName() + " | " + friendsUsername.get(0) + " was added to the hub";
//
////        friendsIds.forEach(id -> {
////            String token = notificationRepository.getNotificationTokenByUserId(id);
////            PushNotificationRequest request = new PushNotificationRequest("PocketApp", message, "", token);
////            pushNotificationService.sendPushNotificationToToken(request);
////        });
//
//
//    }
//
//    @PostMapping("deleteUserFromHubById/{hubId}")
//    public void deleteUserFromHubById(@PathVariable Long hubId, @CurrentUser UserPrincipal currentUser){
//        hubRepository.deleteUserFromHubById(currentUser.getId(), hubId);
//        actionService.deleteUserFromHubAction(currentUser.getUsername(), hubId);
//    }
//
//    @PostMapping("kickUserFromHubById/{hubId}/{friendId}")
//    public void kickUserFromHubById(@PathVariable Long hubId, @PathVariable Long friendId){
//        hubRepository.deleteUserFromHubById(friendId, hubId);
//        actionService.kickUserFromHubAction(hubId, friendId);
//    }
//
//    @GetMapping("/getHubById/{hubId}")
//    public Hub getHubById(@PathVariable Long hubId, @CurrentUser UserPrincipal currentUser){
//        Hub hub = hubRepository.findGroupById(hubId).orElseThrow(
//                () -> new ResourceNotFoundException("Hub", "hubId", hubId));
//        User user = userRepository.findUserById(currentUser.getId()).orElseThrow(
//                () -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
//
//
//        hub.setCreditSum(hub.getDebts().stream()
//                .filter(p -> p.getCreditor().getId().equals(user.getId()))
//                .mapToDouble(d -> {
//                    Double amount = d.getAmount();
//                    Currency currency = d.getCurrency();
//                    if (!currency.name.equals(user.getMainCurrency().name)){ // convert from usd to other
//                        Double fromRate = currencyExchange.getRateByCurrencyName(currency.name);
//                        Double toRate = currencyExchange.getRateByCurrencyName(user.getMainCurrency().name);
//                        d.setAmount(amount * (toRate/fromRate));
//                        d.setCurrency(user.getMainCurrency());
//                    }
//                    return d.getAmount();
//                }).sum());
//
//        hub.setDebitSum(hub.getDebts().stream()
//                .filter(p -> p.getDebitor().getId().equals(currentUser.getId()))
//                .mapToDouble(d -> {
//                    Double amount = d.getAmount();
//                    Currency currency = d.getCurrency();
//                    if (!currency.name.equals(user.getMainCurrency().name)){ // convert from usd to other
//                        Double fromRate = currencyExchange.getRateByCurrencyName(currency.name);
//                        Double toRate = currencyExchange.getRateByCurrencyName(user.getMainCurrency().name);
//                        d.setAmount(amount * (toRate/fromRate));
//                        d.setCurrency(user.getMainCurrency());
//                    }
//                    return d.getAmount();
//                }).sum());
//
//        return hub;
//    }
//
//    @PostMapping("/updateHubById")
//    public void updateHubById(@RequestBody UpdateHubRequest hub){
//        Hub oldHub = hubRepository.findGroupById(hub.getId()).orElseThrow(
//                () -> new ResourceNotFoundException("Hub", "hubId", hub.getId()));
//        hubRepository.updateHubById(hub.getName(),hub.getDescription(), hub.getAvatarUri(), hub.getUserInitiatorId(), Currency.valueOf(hub.getCurrency()), hub.getId());
////        Hub newHub = hubRepository.findGroupById(hub.getId()).orElseThrow(
////                () -> new ResourceNotFoundException("Hub", "hubId", hub.getId()));
//        actionService.editHub(oldHub, hub);
//    }
}
